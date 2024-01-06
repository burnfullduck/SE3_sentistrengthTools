const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack:{
    resolve:{
      fallback :{
        'crypto': require.resolve("crypto-browserify"),
        'stream': require.resolve("stream-browserify")
      }
    }
  },
  devServer: {
    proxy: {
      '/tra': {
        target: 'http://api.fanyi.baidu.com',
        ws:true,
        changeOrigin:true,
        secure: false,
        pathRewrite: {'^/tra':'/api'}
      },
      '/s':{
        target: 'http://124.221.102.208/senti-strength',
        ws:true,
        changeOrigin:true,
        secure: false,
        pathRewrite: {'^/s':''}
      }
    }
  },
})