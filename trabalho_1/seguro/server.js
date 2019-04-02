const express = require('express');

const { createMiner, removeMiner } = require('./data');

const app = express();

app.post('/miner', function(req, res) {
    const name = req.body.name;
    if (name) {
        createMiner(name);
    }
    res.send(200);
});

app.delete('/miner', function(req, res) {
    const name = req.query.name;
    if (name)
    removeMiner(name);
    res.send(200);
});

module.exports = { app }
