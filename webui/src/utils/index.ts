import {dayjs} from "element-plus"


const DarkMode: string = 'dark_mode'


export function handlePageResize(){
    const PH = window.innerHeight
    const AppEle: HTMLHtmlElement | null = document.querySelector('#app')
    if (AppEle != null) {
        AppEle.style.height = PH + 'px'
    }
}

export function changeTheme(isDark: boolean) {
    const htmlEle = document.querySelector('html')
    if (htmlEle != null) {
        if (isDark) {
            htmlEle.classList.add('dark')
            localStorage.setItem(DarkMode, JSON.stringify(true))
        } else {
            htmlEle.classList.remove('dark')
            localStorage.removeItem(DarkMode)
        }
    }
}

export function checkThemeSetting(): boolean {
    return localStorage.getItem(DarkMode) != null
}

export function nowDatetimeFmt(): string {
    return dayjs().format('YYYY-MM-DD HH:mm:ss')
}

export function datetimeFmt(date: Date): string {
    return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

export function closeLoginBox() {
    const loginBox: HTMLHtmlElement | null = document.querySelector('#login-box')
    if (loginBox != null) {
        loginBox.style.display = 'none'
    }
}

export function showLoginBox() {
    const loginBox: HTMLHtmlElement | null = document.querySelector('#login-box')
    if (loginBox != null) {
        loginBox.style.display = 'flex'
    }
}
