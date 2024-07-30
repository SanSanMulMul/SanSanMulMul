//
const mysql = require('mysql2');
const conn = {  
    host: 'localhost',
    port: '3306',
    user:'ssafy',
    password: 'ssafy',
    database: 'test'
};

const connection = mysql.createConnection(conn); 
connection.connect(); 
//

const express = require('express')
const bodyParser = require('body-parser'); //bodyParser추가
const app = express()

//bodyParser 설정
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('', (req, res) => {
    res.sendFile(__dirname + '/index.html')
})

//산 code를 파라미터로 받고, 그 산에 있는 track들 ~ 트랙에있는 (위,경도)값들을 반환함
// (track - track_path들..)
app.get('/track/:code', (req, res) => {
    console.log("산 code : " , req.params);
    connection.query(`SELECT track_path.track_path_lat, track_path.track_path_lon, track_path.track_id 
    FROM track
    JOIN track_path
    ON track.track_id = track_path.track_id
    WHERE track.mountain_code= ?;`,[req.params.code], function (error, results, fields) {
        if (error) throw error;
        // console.log(results);
        res.send(results);
    });
})

//DB insert문 실행
//코스 정보 삽입 (코스 ID, 산ID, 코스 이름, ..)
app.post('/insertCourse', async (req, res) => {
    try {
      const courseId = req.body.courseId; // 코스 ID값(입력해준)
      const mountainCode = req.body.mountainCode; // 산 코드
      const name = req.body.name; // 코스 이름
      const level = req.body.level; // 코스 레벨
      const tracks = req.body.tracks; // [{trackId: 231, sequence: 1}, {trackId: 229, sequence: 2}, ...]
  
      // 코스 정보 삽입
      await new Promise((resolve, reject) => {
        connection.query(
          `INSERT INTO course (course_id, mountain_code, course_name, course_level) VALUES (?, ?, ?, ?);`,
          [courseId, mountainCode, name, level],
          (error, results, fields) => {
            if (error) {
              return reject(error);
            }
            resolve();
          }
        );
      });
  
      // 각 트랙의 길이, 상행시간, 하행시간을 합산
      let totalLength = 0.0;
      let totalUptime = 0;
      let totalDowntime = 0;
  
      for (const track of tracks) {
        // 트랙 정보를 course_tracks에 삽입
        await new Promise((resolve, reject) => {
          connection.query(
            `INSERT INTO course_tracks (course_id, track_id, course_tracks_sequence) VALUES (?, ?, ?);`,
            [courseId, track.trackId, track.sequence],
            (error, results, fields) => {
              if (error) {
                return reject(error);
              }
              resolve();
            }
          );
        });
  
        // track 테이블에서 length, uptime, downtown 가져오기
        const trackData = await new Promise((resolve, reject) => {
          connection.query(
            `SELECT track_length, track_uptime, track_downtime FROM track WHERE track_id = ?;`,
            [track.trackId],
            (error, results, fields) => {
              if (error) {
                return reject(error);
              }
              resolve(results[0]);
            }
          );
        });
  
        totalLength += parseFloat(trackData.track_length);
        totalUptime += trackData.track_uptime;
        totalDowntime += trackData.track_downtime;
      }
  
      // 코스 정보 (길이, 시간) 업데이트
      await new Promise((resolve, reject) => {
        connection.query(
          `UPDATE course SET course_length = ?, course_uptime = ?, course_downtime = ? WHERE course_id = ?;`,
          [totalLength, totalUptime, totalDowntime, courseId],
          (error, results, fields) => {
            if (error) {
              return reject(error);
            }
            resolve();
          }
        );
      });
  
      res.json({ message: 'Course and tracks inserted successfully' });
    } catch (error) {
      res.status(500).send('Error inserting course or tracks');
      console.error(error);
    }
  });
  
app.listen(3000, () => {
    console.log('I love you 3000')
})