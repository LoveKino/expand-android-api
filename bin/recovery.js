#! /usr/bin/env node

var {recovery} = require('..');

var userArgs = process.argv.slice(2);

recovery(userArgs[0], userArgs[1]);
