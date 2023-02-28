import { createRouter, createWebHistory } from 'vue-router'
import JarManagement from "@/views/JarManagement.vue"
import ApplicationManagement from "@/views/ApplicationManagement.vue"


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'application',
      component: ApplicationManagement
    },
    {
      path: '/jar',
      name: 'jar',
      component: JarManagement
    }
  ]
})

export default router
