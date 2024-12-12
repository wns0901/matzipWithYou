document.querySelectorAll('.star').forEach(($star) => {
    $star.addEventListener('click', ({target}) => {
        document.querySelectorAll('.star').forEach(($star) => {
            $star.classList.remove('active');
        });

        target.classList.add('active');
    });
})

const mysql = require('mysql');
const express = require('express');
const app = express();
const port = 8080;

const db = mysql.createConnection({
    host: 'localhost'
    user: 'root',
    password: 'password',
    database: 'matzip_with_you_db'
})

db.connect(err => {
    if(err) {
        console.error('Database connection failed', err.stack);
        return;
    }
    console.log('Connected to database.');
});

app.get('/review', (req, res) => {
    db.query('SELECT id, tagname FROM tag', (err, results))
})
