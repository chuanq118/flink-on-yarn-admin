
type ApplicationState = "NEW" | "NEW_SAVING" | "SUBMITTED" | "ACCEPTED" | "RUNNING" | "FINISHED" | "FAILED" | "KILLED"

export interface YarnApplication{
  "id": number,
  "timestamp": bigint,
  "name": string,
  "type": string,
  "trackingUrl": string,
  "queue": string,
  "launchTime": bigint,
  "state": ApplicationState,
  "stateType"?: "success" | "info" | "warning" | "danger",
  "user": string,
  "host": string

}
