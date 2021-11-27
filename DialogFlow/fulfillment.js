'use strict';

const functions = require('firebase-functions');
const {WebhookClient} = require('dialogflow-fulfillment');
const {Card, Suggestion} = require('dialogflow-fulfillment');
const mysql = require('mysql');

process.env.DEBUG = 'dialogflow:debug'; // enables lib debugging statements

exports.dialogflowFirebaseFulfillment = functions.https.onRequest((request, response) => {
        const agent = new WebhookClient({request, response});
        console.log('Dialogflow Request headers: ' + JSON.stringify(request.headers));
        console.log('Dialogflow Request body: ' + JSON.stringify(request.body));

        /*
         function welcome(agent) {
           agent.add(`Welcome to my agent!`);
         }
        
         function fallback(agent) {
           agent.add(`I didn't understand`);
           agent.add(`I'm sorry, can you try again?`);
         }
        */
        function connectToDatabase() {
            const connection = mysql.createConnection({
                host: 'database-1.cru51w0wfabm.ap-northeast-2.rds.amazonaws.com',
                user: 'admin',
                password: 'konkuk1818',
                database: 'konkuk'
            });

            return new Promise((resolve, reject) => {
                connection.connect();
                resolve(connection);
            });
        }

        function queryShipCode(connection) {
            return new Promise((resolve, reject) => {
                connection.query('SELECT *FROM ShipCode', (error, results, fields) => {
                    resolve(results);
                });
            });
        }

        function queryFrCode(connection) {
            return new Promise((resolve, reject) => {
                connection.query('SELECT *FROM FrCode', (error, results, fields) => {
                    resolve(results);
                });
            });
        }

        function queryCtrCode(connection) {
            return new Promise((resolve, reject) => {
                connection.query('SELECT *FROM CtrCode', (error, results, fields) => {
                    resolve(results);
                });
            });
        }

        function queryCtnCode(connection) {
            return new Promise((resolve, reject) => {
                connection.query('SELECT *FROM CtnCode', (error, results, fields) => {
                    resolve(results);
                });
            });
        }

        function queryAgcCode(connection) {
            return new Promise((resolve, reject) => {
                connection.query('SELECT *FROM AgcCode', (error, results, fields) => {
                    resolve(results);
                });
            });
        }

        function queryPckCode(connection) {
            return new Promise((resolve, reject) => {
                connection.query('SELECT *FROM PckCode', (error, results, fields) => {
                    resolve(results);
                });
            });
        }

        function querystatus_life(connection) {
            return new Promise((resolve, reject) => {
                connection.query('SELECT *FROM lifeequip', (error, results, fields) => {
                    resolve(results);
                });
            });
        }

        function querystatus_work(connection) {
            return new Promise((resolve, reject) => {
                connection.query('SELECT *FROM workplace', (error, results, fields) => {
                    resolve(results);
                });
            });
        }

        function handleCode(agent) {
            agent.setContext({
                name: 'first-call',
                lifespan: 6,
            });
        }

        function handlestatus(agent) {
            agent.setContext({
                name: 'second-call',
                lifespan: 6,
            });
        }

        function handleCode_Ship(agent) {
            agent.getContext('first-call');
            const ShipCode_shipname = agent.parameters.shipname;
            return connectToDatabase()
                .then(connection => {
                    return queryShipCode(connection)
                        .then(result => {
                            console.log(result);
                            result.map(ShipCode => {
                                if (ShipCode_shipname === ShipCode.name) {
                                    agent.add(`ShipCode : ${ShipCode.code} , ShipName : ${ShipCode.name}`);
                                }
                            });
                            connection.end();
                        });
                });
        }

        function handleCode_Fr(agent) {
            agent.getContext('first-call');
            const FrCode_frname = agent.parameters.frname;
            return connectToDatabase()
                .then(connection => {
                    return queryFrCode(connection)
                        .then(result => {
                            console.log(result);
                            result.map(FrCode => {
                                if (FrCode_frname === FrCode.name) {
                                    agent.add(`FrCode : ${FrCode.code} , FrName : ${FrCode.name}`);
                                }
                            });
                            connection.end();
                        });
                });
        }

        function handleCode_Ctr(agent) {
            agent.getContext('first-call');
            const CtrCode_ctrname = agent.parameters.ctrname;
            return connectToDatabase()
                .then(connection => {
                    return queryCtrCode(connection)
                        .then(result => {
                            console.log(result);
                            result.map(CtrCode => {
                                if (CtrCode_ctrname === CtrCode.name) {
                                    agent.add(`CtrCode : ${CtrCode.code} , CtrName : ${CtrCode.name} , CtreName : ${CtrCode.eName} , Ctncode : ${CtrCode.ctncode}`);
                                }
                            });
                            connection.end();
                        });
                });
        }

        function handleCode_Ctn(agent) {
            agent.getContext('first-call');
            const CtnCode_ctnname = agent.parameters.ctnname;
            return connectToDatabase()
                .then(connection => {
                    return queryCtnCode(connection)
                        .then(result => {
                            console.log(result);
                            result.map(CtnCode => {
                                if (CtnCode_ctnname === CtnCode.name) {
                                    agent.add(`CtnCode : ${CtnCode.code} , CtnName : ${CtnCode.name} , Ctn u_name : ${CtnCode.u_name}`);
                                }
                            });
                            connection.end();
                        });
                });
        }

        function handleCode_Agc(agent) {
            agent.getContext('first-call');
            const AgcCode_agcname = agent.parameters.agcname;
            return connectToDatabase()
                .then(connection => {
                    return queryAgcCode(connection)
                        .then(result => {
                            console.log(result);
                            result.map(AgcCode => {
                                if (AgcCode_agcname.concat('\r') === AgcCode.name) {
                                    agent.add(`AgcCode : ${AgcCode.code} , AgcName : ${AgcCode.name}`);
                                }
                            });
                            connection.end();
                        });
                });
        }

        function handleCode_Pck(agent) {
            agent.getContext('first-call');
            const PckCode_pckname = agent.parameters.pckname;
            return connectToDatabase()
                .then(connection => {
                    return queryPckCode(connection)
                        .then(result => {
                            console.log(result);
                            result.map(PckCode => {
                                if (PckCode_pckname.concat('\r') === PckCode.name) {
                                    agent.add(`PckCode : ${PckCode.code} , PckName : ${PckCode.name}`);
                                }
                            });
                            connection.end();
                        });
                });
        }

        function handleStatus_life(agent) {
            agent.getContext('second-call');
            const lifeequip_checknum = agent.parameters.checknum;
            return connectToDatabase()
                .then(connection => {
                    return querystatus_life(connection)
                        .then(result => {
                            console.log("검색완료");
                            result.map(lifeequip => {
                                if (lifeequip_checknum === lifeequip.checknum) {
                                    agent.add(`확인번호 : ${lifeequip.checknum} , 업체명 : ${lifeequip.company} , 대표자명 : ${lifeequip.ceo} , 전화번호 : ${lifeequip.tel} , 주소 : ${lifeequip.address} , 등록일자 : ${lifeequip.regidate}`);

                                }
                            });
                            connection.end();

                        });
                });
        }

        function handleStatus_work(agent) {
            agent.getContext('second-call');
            const workplace_num = agent.parameters.workplace;
            return connectToDatabase()
                .then(connection => {
                    return querystatus_work(connection)
                        .then(result => {
                            console.log("검색완료");
                            result.map(workplace => {

                                if (workplace_num === workplace.num) {
                                    agent.add(`지정번호 : ${workplace.num} , 지정구분 : ${workplace.seg} , 업체명 : ${workplace.company} , 대표자명 : ${workplace.ceo} , 전화번호 : ${workplace.tel} , 주소 : ${workplace.address} , 등록일자 : ${workplace.regidate}`);
                                }
                            });

                            connection.end();
                        });
                });
        }

        let intentMap = new Map();
        //intentMap.set('Default Welcome Intent', welcome);
        //intentMap.set('Default Fallback Intent', fallback);

        intentMap.set('Code', handleCode);
        intentMap.set('status', handlestatus);

        intentMap.set('Code_Ship', handleCode_Ship);
        intentMap.set('Code_Fr', handleCode_Fr);
        intentMap.set('Code_Ctr', handleCode_Ctr);
        intentMap.set('Code_Ctn', handleCode_Ctn);
        intentMap.set('Code_Agc', handleCode_Agc);
        intentMap.set('Code_Pck', handleCode_Pck);

        intentMap.set('status_life', handleStatus_life);
        intentMap.set('status_work', handleStatus_work);

        agent.handleRequest(intentMap);
    }
);