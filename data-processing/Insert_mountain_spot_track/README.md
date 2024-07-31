# 데이터 저장 및 전처리 (python)

1. **데이터를 저장할 DB 및 테이블 생성**
    mountain_spot.sql : mountain, mountain_spot 테이블 생성
    tr
    track_path_course_path_script.sql : 구간테이블 track, track_path / 코스테이블 course, course_tracks 생성


2. **다음 코드을 자신의 DB정보에 맞게 바꿔주기**
    * mountain.py
        ```jsx
        engine = create_engine('mysql+mysqlconnector://[USER]:[PASSWORD]@[DB호스트]:3306/[DB NAMNE]')
        ```
    * spot.py, track.py
        ```jsx
        conn = pymysql.connect(host='[DB호스트]', port=3306, user='[USER]', password='[PASSWORD]', db='[DB NAME]', charset='utf8')
        ```

3. **다음과 같이 로그가 나오면 성공입니다**

    산 스팟 데이터(spot.py) => ------------spot데이터 DB저장 완료------------ <br>
    산 구간 데이터(track.py) => ------------산 전체 구간 데이터 DB저장 완료------------



4. **sql파일 뽑는 방법**
    ```jsx
    mysqldump.exe -u [사용자 계정] -p [백업하고자 하는 DB 이름] > [생성될 백업 DB 이름].sql
    ```
    모든 파일을 입력한 후에 해주세요.
    1. 해당 명령어는 cmd로 해주세요
    2. 해당 mysqldump.exe 파일은 mysql 저장 폴더에서 bin폴더 안에 있습니다.
    3. 해당 데이테베이스가 다 뽑아오는 것이므로 주의해 주세요


