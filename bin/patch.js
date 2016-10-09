#! /usr/bin/env node

var {patch} = require('..');

var userArgs = process.argv.slice(2);

patch(userArgs[0], userArgs[1]);
