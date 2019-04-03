const express = require('express');
const fileUpload = require('express-fileupload');
const bodyParser = require('body-parser');

const { createMiner, removeMiner, MINERS_PATH, loadMiner } = require('./data');

const app = express();
app.use(fileUpload());
app.use(bodyParser.urlencoded({ extended: true }))

app.post('/miner', function(req, res) {
    const name = req.body.name;
    if (name) {
        createMiner(name);
    }
    res.status(200).send();
});

app.post('/miner/remove', function(req, res) {
    const name = req.body.name;
    if (name)
    removeMiner(name);
    res.status(200).send();
});

app.get('/miner/export', function (req, res) {
    res.download(`${MINERS_PATH}/${req.query.name}.miner`);
});

app.post('/miner/import', function (req, res) {
    const importFile = req.files.importFile;
    importFile.mv(`${MINERS_PATH}/${importFile.name}`, function(err) {
        if (Object.keys(req.files).length == 0) {
            return res.status(400).send('No files were uploaded.');
        }
        if (err)
            return res.status(500).send(err);
    
        res.status(200).send();
    });

    loadMiner(importFile.name);
});

module.exports = { app }
