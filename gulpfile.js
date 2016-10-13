'use strict';

let gulp = require('gulp');
let promisify = require('promisify-node');
let fs = promisify('fs');
let path = require('path');
let _mkdirp = require('mkdirp');
let mkdirp = promisify(function(r, callback) {
    _mkdirp(r, callback);
});

const DEV_PATCH = path.join(__dirname, './AndroidContainer');

const AOSP_PATCH_DEV = path.join(__dirname, './src/aospPatchDev');

let copyFile = (file1, file2) => {
    return fs.readFile(file1, 'utf-8').then((data) => {
        // mkdir first
        return mkdirp(path.dirname(file2)).then(() => {
            return fs.writeFile(file2, data, 'utf-8');
        });
    });
};

gulp.task('buildPatchSource', () => {
    return Promise.all([
        [
            'aosppatch/src/main/java/com/android/freekite/patch/aosppatch/PatchReadHookSource.java',
            'android-7.0.0_r6/src/PatchReadHookSource.java'
        ],
        [
            'aosppatch/src2/android-7.0.0_r6/Activity.java',
            'android-7.0.0_r6/src/Activity.java'
        ],
        [
            'aosppatch/src2/android-7.0.0_r6/DecorView.java',
            'android-7.0.0_r6/src/DecorView.java'
        ]
    ].map(([f1, f2]) => copyFile(
        path.join(DEV_PATCH, f1),
        path.join(AOSP_PATCH_DEV, f2)
    )));
});
