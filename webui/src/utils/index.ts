import {dayjs} from "element-plus"


const DarkMode: string = 'dark_mode'


export function handlePageResize(){
    const PH = window.innerHeight
    const AppEle: HTMLHtmlElement | null = document.querySelector('#app')
    if (AppEle != null) {
        AppEle.style.height = PH + 'px'
    }
  const TableBlock: HTMLHtmlElement | null = document.querySelector('#flink-yarn-table-block > .el-table')
  if (TableBlock != null) {
    // console.log('change table block')
    TableBlock.style.height = (PH - 80 - 80) + 'px'
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


// 定义 IP 地址和主机名的对应关系
const ipMapping: { [hostname: string]: string } = {
  'node30': '111.56.16.62',
  'node31': '111.56.16.62',
  'node32': '111.56.16.62',
  'node33': '111.56.16.62',
  'wq31': '39.153.214.14',
  'wq32': '39.153.214.14',
  'wq33': '39.153.214.14'
};

export function ReplaceHostnameWithOutboundIp(url : string) :string {

  // 解析 URL 获取主机名
  const urlObj = new URL(url);
  const hostname = urlObj.hostname;

  // 替换主机名为对应的 IP 地址
  const ip = ipMapping[hostname];

  return ip ? url.replace(hostname, ip) : url;
}

