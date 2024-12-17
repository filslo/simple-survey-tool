import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        name : 'surveys',
        path: '/',
        component: () => import('./components/Surveys.vue')
    },
    {
        name : 'survey',
        path: '/surveys/:id',
        component: () => import('./components/Survey.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

createApp(App)
    .use(router)
    .mount('#app')
