import pandas as pd
from sqlalchemy import create_engine, text
import json
# 설치
# pip install pandas
# pip install sqlalchemy
# pip install mysql-connector-python


# JSON 파일 읽기
with open('./mountain/mountain.json', 'r', encoding='utf-8') as file:
    data = json.load(file)

# JSON 데이터를 데이터프레임으로 변환
df = pd.DataFrame(data)

# 데이터프레임의 컬럼명 변경
df = df.rename(columns={
    "번호": "mountain_id",
    "산코드": "mountain_code",
    "산이름": "mountain_name",
    "소재지": "mountain_location",
    "높이(m)": "mountain_height",
    "설명": "mountain_description",
    "주요계절": "mountain_weather",
    "위도": "mountain_lat",
    "경도": "mountain_lon"
})

# MySQL 데이터베이스 연결 설정 * DB 정보에 맞게 변경 필수 *
engine = create_engine('mysql+mysqlconnector://ssafy:1234@localhost:3306/sansanmulmul')

# 데이터베이스에 데이터 삽입
df.to_sql(name='mountain', con=engine, if_exists='append', index=False)

