import EventEmitter from "eventemitter3"

interface StreamReaderConfig {
    bufferLength: number
    retryInterval: number
}

class StreamReader<T> {
    protected EE = new EventEmitter()
    protected url: string = ''
    protected config: StreamReaderConfig
    protected innerBuffer : T[] = []
    protected conn: WebSocket | null = null

    constructor(cfg: {bufferLength?: number, retryInterval?: number}) {
        this.config = Object.assign({bufferLength: 0, retryInterval: 5000}, cfg)
    }

    protected connectWebsocket () {
        if (!this.url) {
            return
        }

        const url = new URL(this.url)

        this.conn = new WebSocket(url.toString())
        this.conn.addEventListener('message', msg => {
            const data = JSON.parse(msg.data)
            this.EE.emit('data', [data])
            if (this.config.bufferLength > 0) {
                this.innerBuffer.push(data)
                if (this.innerBuffer.length > this.config.bufferLength) {
                    this.innerBuffer.splice(0, this.innerBuffer.length - this.config.bufferLength)
                }
            }
        })

        this.conn.addEventListener('error', err => {
            this.EE.emit('error', err)
            this.conn?.close()
            setTimeout(this.connectWebsocket, this.config.retryInterval)
        })
    }

    connect(url: string) {
        if (this.url === url && this.conn) {
            return
        }
        this.url = url
        this.conn?.close()
        this.connectWebsocket()
    }

    hasConnected(): boolean{
        return this.conn != null
    }

    subscribe(event: string, callback: (data: T[]) => void){
        this.EE.addListener(event, callback)
    }

    unsubscribe(event: string, callback: (data: T[]) => void) {
        this.EE.removeListener(event, callback)
    }

    buffer() {
        return this.innerBuffer.slice()
    }

    destroy () {
        this.EE.removeAllListeners()
        this.conn?.close()
        this.conn = null
    }
}
