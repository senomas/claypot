// server/app.js
const express = require('express');
const morgan = require('morgan');
const path = require('path');

const app = express();

// Setup logger
app.use(morgan(':remote-addr - :remote-user [:date[clf]] ":method :url HTTP/:http-version" :status :res[content-length] :response-time ms'));

// Serve static assets
app.use(express.static(path.resolve(__dirname, '..', 'build')));

app.post('/api/v1.0/blog/list', function(req, res) {
   var options = {
      host:   'localhost',
      port:   8080,
      path:   '/api/v1.0/blog/list',
      method: 'POST',
      headers: req.headers
   };
   var creq = http.request(options, function(cres) {
      cres.setEncoding('utf8');
      cres.on('data', function(chunk){
         res.write(chunk);
      });
      cres.on('close', function(){
         res.writeHead(cres.statusCode);
         res.end();
      });
      cres.on('end', function(){
         res.writeHead(cres.statusCode);
         res.end();
      });
   }).on('error', function(e) {
      console.log(e.message);
      res.writeHead(500);
      res.end();
   });
   creq.end();
});

// Always return the main index.html, so react-router render the route in the client
app.get('*', (req, res) => {
  res.sendFile(path.resolve(__dirname, '..', 'build', 'index.html'));
});

module.exports = app;
