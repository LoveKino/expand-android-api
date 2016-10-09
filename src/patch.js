'use strict';

let promisify = require('promisify-node');
let fs = promisify('fs');
let path = require('path');
let {
    map
} = require('bolzano');
let {
    existsFile
} = require('system-tool');
let del = require('del');
let _mkdirp = require('mkdirp');
let mkdirp = promisify(function(r, callback){
    _mkdirp(r, callback);
});

/**
 * reflect files
 * [source, dist] backup
 */
let patch = (rels, backupDir) => {
    return Promise.resolve(map(rels, ([source, dist, type]) => {
        // backup first
        if (type === 'add') {
            return copyFile(dist, source);
        } else {
            return backup(source, backupDir).then(() => {
                // copy
                return copyFile(dist, source);
            });
        }
    }));
};

let recovery = (rels, backupDir) => {
    return Promise.resolve(map(rels, ([source, dist, type]) => { // eslint-disable-line
        if (type === 'add') {
            // remove
            return del([source], {
                force: true
            });
        } else {
            let backupFilePath = path.join(backupDir, filePathToName(source));
            return copyFile(backupFilePath, source);
        }
    }));
};

let backup = (source, backupDir) => {
    let backupFilePath = path.join(backupDir, filePathToName(source));
    // only backup once
    // may reflect multiple times
    return existsFile(backupFilePath).then((ret) => {
        if (!ret) {
            return copyFile(source, backupFilePath);
        }
    });
};

let filePathToName = (file) => file.replace(new RegExp(path.sep, 'g'), '_');

let copyFile = (file1, file2) => {
    return fs.readFile(file1, 'utf-8').then((data) => {
        // mkdir first
        return mkdirp(path.dirname(file2)).then(() => {
            return fs.writeFile(file2, data, 'utf-8');
        });
    });
};

module.exports = {
    patch,
    recovery
};
