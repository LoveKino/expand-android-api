'use strict';

let path = require('path');
let {
    patch, recovery
} = require('./patch');

let {
    map
} = require('bolzano');

/**
 * aosp
 */
let AOSP_PATCH = path.join(__dirname, './aospPatchDev');

// TODO support add and delete source file

let filesMap = {
    'android-7.0.0_r6': [
        [
            'frameworks/base/core/java/android/app/Activity.java',
            'Activity.java'
        ],
        [
            'frameworks/base/core/java/com/android/freekite/patch/aosppatch/PatchReadHookSource.java',
            'PatchReadHookSource.java',
            'add'
        ],
        [
            'frameworks/base/core/java/com/android/internal/policy/DecorView.java',
            'DecorView.java'
        ]
    ]
};

let getFilesReflect = (aospPath, version) => {
    let fileRels = filesMap[version] || [];
    return map(fileRels, ([source, dist, type]) => {
        return [
            path.join(aospPath, source),
            path.join(AOSP_PATCH, version, 'src', dist),
            type
        ];
    });
};

module.exports = {
    patch: (aospPath, version) => {
        return patch(
            getFilesReflect(aospPath, version),
            path.join(AOSP_PATCH, version, 'backup')
        );
    },
    recovery: (aospPath, version) => {
        return recovery(
            getFilesReflect(aospPath, version),
            path.join(AOSP_PATCH, version, 'backup')
        );
    }
};
