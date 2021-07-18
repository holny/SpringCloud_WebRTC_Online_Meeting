const path = require('path')
const name = 'vue Element Admin' // page title
const IS_PROD = ["production", "prod"].includes(process.env.NODE_ENV);
function resolve (dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  publicPath: IS_PROD ? process.env.VUE_APP_PUBLIC_PATH : "/", // 基本路径
  outputDir: 'dist', // 输出文件目录
  lintOnSave: process.env.NODE_ENV === 'development', // eslint-loader 是否在保存的时候检查
  // see https://github.com/vuejs/vue-cli/blob/dev/docs/webpack.md
  // webpack配置
  chainWebpack: (config) => {
  },
  configureWebpack: {
    // provide the app's title in webpack's name field, so that
    // it can be accessed in index.html to inject the correct title.
    name: name,
    resolve:{
      alias:{
        '@': resolve('src')
      }
    }
  },
  // 生产环境是否生成 sourceMap 文件
  productionSourceMap: !IS_PROD,
  // 是否为 Babel 或 TypeScript 使用 thread-loader。
  //该选项在系统的 CPU 有多于一个内核时自动启用，仅作用于生产构建。
  parallel: require('os').cpus().length > 1,
  // PWA 插件相关配置 see
  //https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-pwa
  pwa: {},
  // webpack-dev-server 相关配置
  devServer: {
    open: process.platform === 'darwin',
    // host: '0.0.0.0', // 允许外部ip访问
    host: process.env.VUE_APP_HOST,
    port: process.env.VUE_APP_PORT, // 端口
    https: false, // 启用https
    sockHost: process.env.VUE_APP_BACK_END_API,
    disableHostCheck: true,
    overlay: {
      warnings: true,
      errors: true
    }, // 错误、警告在页面弹出
    proxy: {
      '/api': {
        target: process.env.VUE_APP_BACK_END_API,
        changeOrigin: true, // 开启代理，在本地创建一个虚拟服务端
        ws: true,
        pathRewrite: {
          '^/api': ''
        }
      },

    } // 代理转发配置，用于调试环境
  },
  // 第三方插件配置
  pluginOptions: {
    quasar: {
      importStrategy: 'kebab',
      rtlSupport: false
    }
  },
  transpileDependencies: [
    'quasar'
  ]
}
