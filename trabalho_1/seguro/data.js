const fs = require('fs');
const { Miner } = require('./miner');

const MINERS_PATH = './mineradores';

const createPath = () => {
    if (!fs.existsSync(MINERS_PATH)){
        fs.mkdirSync(MINERS_PATH);
    }
}

const serialize = miner => {
    const action = String(miner.startWork).replace(/\r?\n|\r/g, '');
    return `{ "name": "${ miner.name }", "startWork": "${ action }" }`;
}
const deserialize = content => {
    const obj = JSON.parse(content);
    obj.startWork = eval(obj.startWork);
    return obj;
}

const createMiner = name => {
    const miner = Miner(name);
    createPath();
    fs.writeFile(`${MINERS_PATH}/${name}.json`, serialize(miner), function(err) {
        if(err) return console.log(err);
    }); 
    miner.startWork();
}

const removeMiner = name => {
    createPath();
    fs.unlink(`${MINERS_PATH}/${name}.json`);
}


const loadMiners = () => {
    createPath();
    fs.readdir(MINERS_PATH, function (err, files) {
        if (err) {
            return console.log('Unable to scan directory: ' + err);
        }
        files.forEach(file => loadMiner(file));
    })
}

const loadMiner = file => {
    fs.readFile(`${MINERS_PATH}/${file}`, {encoding: 'utf-8'}, function(err, data){
        if (!err) {
            const miner = deserialize(data);
            miner.startWork();
        } else {
            console.log('Error on load miner ' + err);
        }
    });
}

module.exports = { createMiner, loadMiners, removeMiner }
