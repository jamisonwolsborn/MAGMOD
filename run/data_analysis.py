from json.tool import main
import magpylib as magpy
import sqlite3
import pandas as pd
import numpy as np

# gets the magnetic moments and coordinates of any magnet blocks that are immediate neighbors
def look_at_neighbors(x, y, z,bounds):
    moments = np.empty([0,3])
    neighbors = pos[(pos[0] <= x + bounds) & (pos[0] >= x - bounds) &
                     (pos[1] <= y + bounds) & (pos[1] >= y - bounds) &
                     (pos[2] <= z + bounds) & (pos[2] >= z - bounds)]
    
    for i in neighbors.index:
        moments = np.append(moments, [m.loc[i]], axis = 0)
    return moments, neighbors

# finds the magnet blocks that are in the 27 3x3x3 block cubes and returns the 27 center points
# also returns the 27 aggregated magnetic moments of the blocks
def a_block_away(x, y, z, bounds):    
    moments = np.empty([0,3])
    centers = np.empty([0,3])
    for i in range(-1,2):
        for j in range(-1,2):
            for k in range(-1,2):
                block = pos[(pos[0] <= x + bounds + 4 * i) & (pos[0] >= x - bounds + 4 * i) &
                            (pos[1] <= y + bounds + 4 * j) & (pos[1] >= y - bounds + 4 * j) &
                            (pos[2] <= z + bounds + 4 * k) & (pos[2] >= z - bounds + 4 * k)]
                moment = m.loc[block.index].sum().to_numpy()
                moments = np.append(moments, [moment], axis = 0)
         
    for i in range(-1,2):
        for j in range(-1,2):
            for k in range(-1,2):
                center = np.array([(x + 4*i), (y + 4*j), (z + 4*k)])
                centers = np.append(centers, [center], axis = 0)
    
    centers = np.delete(centers, 13, 0)
    moments = np.delete(moments, 13, 0)
    return moments, centers

# combines neighbors and 3x3x3 cube centers to the 
def prepare_df(x,y,z,bounds):
    
    moments, centers = a_block_away(x,y,z,bounds)
    neighbor_moments, neighbors = look_at_neighbors(x,y,z,bounds)
    centers = pd.concat([pd.DataFrame(centers), pd.DataFrame(neighbors)], ignore_index=True)
    moments = pd.concat([pd.DataFrame(moments), pd.DataFrame(neighbor_moments)], ignore_index=True)
    
    return centers, moments

def get_coords(pos):
    coords = pd.DataFrame({"x":[], "y":[], "z":[]})
    for index in pos.index:
        for i in range(-3,4):
            for j in range(-3,4):
                for k in range(-3,4):
                    x = pos[0].loc[index] + i
                    y = pos[1].loc[index] + j
                    z = pos[2].loc[index] + k
                    if coords[(coords["x"] == i) & (coords["y"] == j) & (coords["z"] == k)].empty:
                        coords_ = pd.DataFrame({"x":[x], "y":[y], "z":[z]})
                        coords = pd.concat([coords, coords_], ignore_index=True)
    return coords

def main():
    con = sqlite3.connect('database.db')

    df = pd.read_sql_query("SELECT * from mag_positions", con)
    df['Timestamp'] = pd.to_datetime(df['Timestamp'])
    df = df[df['Timestamp'] - pd.Timestamp.now() < pd.Timedelta(5, "hours")]
    df.drop_duplicates(subset = ['pos'], inplace=True)

    global pos
    pos = df['pos'].str.split(",", expand=True).astype(float)
    global m
    m = df['m'].str.split(",", expand=True).astype(float)

    coords = get_coords(pos)
    mag_fields = np.empty([0,3])
    for i in coords.index:
        x = coords["x"].loc[i]
        y = coords["y"].loc[i]
        z = coords["z"].loc[i]
        centers, moments = prepare_df(x,y,z,1)
        magnets = []
        for i in centers.index:
            magnets.append(magpy.magnet.Cuboid(magnetization=(moments[0].loc[i], moments[1].loc[i], moments[2].loc[i]),
                                               dimension = (1000, 1000, 1000),
                                               position=(centers[0].loc[i]*1000, centers[1].loc[i]*1000, centers[2].loc[i]*1000)))
        sensor = magpy.Sensor(position=(x*1000, y*1000, z*1000))
        B = sensor.getB(magnets, sumup = True)
        mag_fields = np.append(mag_fields, [B], axis = 0)
    
    coords_df = pd.DataFrame(coords)
    mag_field_df = pd.DataFrame(mag_fields)
    output_df = coords_df.join(mag_field_df)

    coords_df = pd.DataFrame(coords)
    mag_field_df = pd.DataFrame(mag_fields)
    output_df = coords_df.join(mag_field_df)

    output_df['pos'] = output_df['x'].astype(str) + "," + output_df['y'].astype(str) + "," + output_df['z'].astype(str)
    output_df['mag_field'] = output_df[0].astype(str) + "," + output_df[1].astype(str) + "," + output_df[2].astype(str)

    output_df[['pos','mag_field']].to_sql("mag_field", con = con, if_exists='append', index = False)

if __name__ == "__main__":
    main()