import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//导入bootstrap
import "bootstrap/dist/css/bootstrap.css"


createApp(App).use(ElementPlus).use(router).mount('#app')

