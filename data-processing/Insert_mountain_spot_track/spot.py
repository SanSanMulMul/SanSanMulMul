import json
import pymysql
from pyproj import Transformer
import os

dataset_type ="+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs"
latAndLong_type ="+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs "

transformer = Transformer.from_crs(dataset_type, latAndLong_type)

# DB정보에 맞게 변경
conn = pymysql.connect(host='localhost',  port=3306, user='ssafy', password='1234', db='sansanmulmul', charset='utf8')

cursor = conn.cursor()

def jsonToCsv_spot(file_path):
    # print(file_path)
    with open(file_path, 'r',encoding="UTF8") as file:
        data = json.load(file)
        for feat in data["features"]:
            # print(feat["attributes"]) 
            # print("-----------------------------")
            MNTN_CODE = feat["attributes"]["MNTN_CODE"] # 산코드 mountain_code
            # print(type(MNTN_CODE))
            # MNTN_CODE = int(MNTN_CODE)
            MNTN_NM = feat["attributes"]["MNTN_NM"] #  mountain_name
            MANAGE_SP1 = feat["attributes"]["MANAGE_SP1"] #  01, 02, 03, .. 
            MANAGE_SP2 = feat["attributes"]["MANAGE_SP2"] #  시종점
            DETAIL_SPO = feat["attributes"]["DETAIL_SPO"] #  시종점

            x = feat["geometry"]["x"] 
            y = feat["geometry"]["y"] 
            # 좌표계가 달라서 위도,경도를 국토지리정보원 좌표계로 변환
            x,y=transformer.transform(x,y)

            cursor.execute("insert into mountain_spot (`mountain_code`, `mountain_spot_lat`,`mountain_spot_lon`,\
            `mountain_spot_type_id`, `mountain_spot_type`, `mountain_spot_detail`) values (%s,%s,%s,%s,%s,%s)",(MNTN_CODE,y,x,MANAGE_SP1,MANAGE_SP2,DETAIL_SPO))


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    os.chdir('./spot')
    file_names = os.listdir()
    
    idx = 1
    for filename in file_names:
        print("이번 file : " , filename, idx)
        idx += 1
        jsonToCsv_spot(filename)
        conn.commit()

    conn.close()
    print("------------spot데이터 DB저장 완료------------")

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
