const { app } = require('./server');
const { loadMiners } = require('./data');

loadMiners();

app.listen(8000, () => {
    console.log(`application is running at: http://localhost:8000`);
});