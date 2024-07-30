import json
import pymysql
from pyproj import Transformer
import os
# import requests
# import rasterio

dataset_type ="+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs"
latAndLong_type ="+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs "

transformer = Transformer.from_crs(dataset_type, latAndLong_type)

# DB정보에 맞게 변경
conn = pymysql.connect(host='localhost',  port=3306, user='ssafy', password='1234', db='sansanmulmul', charset='utf8')

cursor = conn.cursor()

def jsonToCsv_trail(file_path):

    with open(file_path, 'r',encoding="UTF8") as file:
        data = json.load(file)
        for feat in data["features"]:
            # print(feat["attributes"])
            MNTN_CODE = feat["attributes"]["MNTN_CODE"] # 산코드 mountain_code
            PMNTN_SN = feat["attributes"]["PMNTN_SN"] # no
            PMNTN_NM = feat["attributes"]["PMNTN_NM"] #  구간 이름 name
            PMNTN_LT = feat["attributes"]["PMNTN_LT"] #  length
            PMNTN_DFFL = feat["attributes"]["PMNTN_DFFL"] #  level
            PMNTN_UPPL = feat["attributes"]["PMNTN_UPPL"] #  uptime
            PMNTN_GODN = feat["attributes"]["PMNTN_GODN"] #  downtime

            # 구간 난이도 db에 맞게, ENUM('EASY', 'MEDIUM', 'HARD') 로 변경
            if PMNTN_DFFL == "쉬움" :
                PMNTN_DFFL = "EASY"
            elif PMNTN_DFFL == "중간" :
                PMNTN_DFFL = "MEDIUM" 
            else : 
                PMNTN_DFFL = "HARD"

            # 구간 테이블(track) 에 INSERT
            cursor.execute("INSERT INTO track (`mountain_code`,`track_no`,`track_name`,`track_length`,"+
                        "`track_level`,`track_uptime`,`track_downtime`) VALUES (%s,%s,%s,%s,%s,%s,%s)", 
                        (MNTN_CODE,PMNTN_SN,PMNTN_NM,PMNTN_LT,PMNTN_DFFL,PMNTN_UPPL,PMNTN_GODN))
            track_id = cursor.lastrowid # 현재 구간(track)의 track_id
            best_path_id = 0; # 이 트랙에서 가장 대표가되는 좌표값 id저장 (track_path_id값)

            # 이 구간(트랙) 내 좌표값(위,경도) 쌍들
            for paths in feat["geometry"]["paths"]:
                for path in paths:
                    x,y=transformer.transform(path[0],path[1])

                    cursor.execute("INSERT INTO track_path (`track_path_lat`,`track_path_lon`,`track_id`) VALUES (%s,%s,%s)",(y,x,track_id))
                    best_path_id = cursor.lastrowid # 이 트랙에의 가장 마지막 좌표값 (track_path_id값) 저장됨
            cursor.execute("UPDATE track SET track_best_path = %s WHERE track_id = %s",(best_path_id,track_id))


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    os.chdir('./track') 
    file_names = os.listdir()
    # print(dataset.bounds)
    
    idx = 1
    for filename in file_names:
        print("이번 file : " , filename, idx)
        idx += 1
        jsonToCsv_trail(filename)
        conn.commit()

    conn.close()
    print("------------산 전체 구간 데이터 DB저장 완료------------")

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
