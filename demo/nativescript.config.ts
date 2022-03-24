import { NativeScriptConfig } from '@nativescript/core';

export default {
  android: {
    id: 'za.co.digitlab.GFConnect',
    v8Flags: '--expose_gc',
    markingMode: 'none'
  },
  ios: {
    id: 'gfconnect'
  },
  appPath: 'app',
  appResourcesPath: 'App_Resources'
} as NativeScriptConfig;