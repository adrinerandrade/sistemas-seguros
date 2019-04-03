const fs = require('fs');
const { Miner } = require('./miner');
const { encrypt, decrypt } = require('./crypto');

const MINERS_PATH = './mineradores';

const createPath = () => {
    if (!fs.existsSync(MINERS_PATH)){
        fs.mkdirSync(MINERS_PATH);
    }
}

const serialize = miner => {
    const action = String(miner.startWork).replace(/\r?\n|\r/g, '');
    return encrypt(`{ "name": "${ miner.name }", "startWork": "${ action }" }`);
}
const deserialize = content => {
    const obj = JSON.parse(decrypt(content));
    obj.startWork = eval(obj.startWork);
    return obj;
}

const createMiner = name => {
    const miner = Miner(name);
    createPath();
    fs.writeFile(`${MINERS_PATH}/${name}.miner`, serialize(miner), function(err) {
        if(err) return console.log(err);
    }); 
    miner.startWork();
}

const removeMiner = name => {
    createPath();
    fs.unlink(`${MINERS_PATH}/${name}.miner`);
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

module.exports = { createMiner, loadMiner, loadMiners, removeMiner, MINERS_PATH }
