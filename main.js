var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');
var app = express();

app.use(bodyParser.json({extended: true}));
app.use(bodyParser.urlencoded({extended: true}));

app.listen(3000,  function () {
    console.log('서버 실행 중...');
});

var connection = mysql.createConnection({
    host: "database-1.cru51w0wfabm.ap-northeast-2.rds.amazonaws.com",
    user: "admin",
    database: "konkuk",
    password: "konkuk1818",
    port: 3306
});



app.post('/user/join', function (req, res) {
    console.log(req.body);
    var id = req.body.id;
    var passwd = req.body.passwd;
    var name = req.body.name;
    var tel = req.body.tel;
    var hint_A = req.body.hint_A;
    var hint_Q = req.body.hint_Q;

    // 삽입을 수행하는 sql문.
    var sql = 'INSERT INTO user (id, passwd, name, tel, hint_A, hint_Q) VALUES (?, ?, ?, ?, ?, ?)';
    var params = [id, passwd, name, tel, hint_A, hint_Q];

    // sql 문의 ?는 두번째 매개변수로 넘겨진 params의 값으로 치환된다.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = '회원가입에 성공했습니다.';
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

app.post('/user/login', function (req, res) {
    var id = req.body.id;
    var passwd = req.body.passwd;
    var sql = 'select * from user where id = ?';
    var params = [id, passwd];


    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';
        var name = '';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '존재하지 않는 계정입니다!';
            } else if (passwd !== result[0].passwd) {
                resultCode = 204;
                message = '비밀번호가 틀렸습니다!';
            } else {
                resultCode = 200;
                message = '로그인 성공! ' + result[0].name + '님 환영합니다!';
                name = result[0].name;
            }
        }

        res.json({
            'code': resultCode,
            'message': message,
            'id': id,
            'name' : name
        });


    })
});

app.post('/user/findID', function (req, res) {
    var name = req.body.name;
    var tel = req.body.tel;
    var sql = 'select * from user where name = ?';
    var params = [name, tel];

    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '존재하지 않는 계정입니다!';
            } else if (tel !== result[0].tel) {
                resultCode = 204;
                message = '입력하신 전화번호를 다시 확인해주세요!';
            } else {
                resultCode = 200;
                message = result[0].name + '님의 아이디는 ' + result[0].id + '입니다!';
            }
        }

        res.json({
            'code': resultCode,
            'message': message,
        });


    })
});

app.post('/user/findQ', function (req, res) {
    var id = req.body.id;
    var name = req.body.name;
    var sql = 'select * from user where id = ?';
    var params = [id, name];

    connection.query(sql, params, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다';

            if (err) {
                    console.log(err);
            } else {
                    if (result.length === 0) {
                            resultCode = 204;
                            message = '존재하지 않는 계정입니다!';
                    } else if (name !== result[0].name) {
                            resultCode = 204;
                            message = '입력하신 이름을 다시 확인해주세요!';
                    } else {
                            resultCode = 200;
                            message = result[0].hint_Q;
                    }
            }

            res.json ({
                    'code' : resultCode,
                    'message' : message
            });

    })
});

app.post('/user/findPW', function (req, res) {
    var hint_A = req.body.hint_A;
    var sql = 'select * from user where hint_A = ?';

    connection.query(sql, hint_A, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';


        if (err) {
            console.log(err);
        } else {
		if (result.length === 0) {
			resultCode = 204;
                	message = '비밀번호 힌트가 틀렸습니다!';
       	    	 }else {
			resultCode = 200;
			message = result[0].name + '님의 비밀번호는 ' + result[0].passwd + '입니다!';                
       		 }
	}

        res.json({
            'code': resultCode,
            'message': message
        });


    })
});


app.post('/user/delete', function (req, res) {
    var passwd = req.body.passwd;
    var id= req.body.id;

    var sql = 'select * from user where passwd = ? and id =?';
    var params = [passwd, id];

    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '아무것도 아님';


        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '비밀번호가 틀렸습니다';
            }else {
                var id = result[0].id;
               
                var sql2 = 'delete from user where id = ?';
                connection.query(sql2,[id], function (err, result) {
		if(err){
                        message ='계정삭제 오류 발생';
                 }
                   
                });
                resultCode = 200; //탈퇴 성공
                message='탈퇴가 완료되었습니다';
	    }
	}	

        res.json({
            'code': resultCode,
            'message': message,
        });


     });
});


app.post('/user/checkID', function (req, res) {
    var id = req.body.id;

    var sql = 'select * from user where id = ?';
    var params = [id];

    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '에러가 발생했습니다';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 200;
               // message = '사용할 수 있는 아이디 입니다';
            } else if(id == result[0].id){ // 해당 아이디 존재
                resultCode = 204;
               // message = '해당하는 아이디가 존재합니다.';
            }
        }

        res.json({
            'code': resultCode,
            'message': message,
        });


    });
});
