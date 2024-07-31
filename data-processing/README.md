## 데이터 전처리


### 📁 Insert_mountain_spot_track
📃 데이터 출처 : 산림청 공공데이터 - 등산로 정보 
<br>
* mountain 데이터 (.csv)
* mountain에 있는 spot 데이터 (.json)
* mountain에 있는 track(구간) 데이터 (.json)
위 데이터들을 각각 mountain테이블, mountain_spot테이블, mountain_track테이블에 넣는다.
### 📁 make_course
    Express로 구현한 코스 생성 사이트.
    위에서 DB에 저장한 track정보를 불러와서 화면에 띄워준 다음,
    생성할 course에 해당하는 track들을 눌러서 course를 생성한다. 
    생성한 course는 필요조건에 맞게 전처리 과정을 거친 후 mountain_course테이블에 정보가 저장된다.
    - 코스 길이, 상행시간, 하행시간, 난이도, 사용되는 트랙들

## 📁 폴더 구조
```
📁 Insert_mountain_spot_track
┣ 📂 mountain
┃ ┗ 📝 mountain.json
┃
┣ 📂 spot
┃ ┣ 📝 PMNTN_SPOT_가리산.json
┃ ┣ 📝 PMNTN_SPOT_가리왕산.json
┃ ┣ 📝 ...
┃ ┗ 📝 PMNTN_SPOT_희양산.json
┃
┣📂 track
┃ ┣ 📝 PMNTN_가리산.json
┃ ┣ 📝 PMNTN_가리왕산.json
┃ ┣ 📝 ...
┃ ┗ 📝 PMNTN_희양산.json
┃
┣ mountain.py
┣ spot.py
┣ track.py
┣ mountain_spot.sql
┣ track_path_course_path_script.sql
┗ 📝 README.md
```


```
📁 make_course
┣ app.js
┣ index.html
┗ 📝 README.md
```