import axios from "axios";

export const startURL_frontend_local: string = "localhost:5173";
export const startURL_backend_local: string = "http://localhost:8080";
export const oAuth2_segment_github: string = "/oauth2/authorization/github";

export default function login() {
    const host: string =
        window.location.host === startURL_frontend_local ?
            startURL_backend_local :
            window.location.origin;
    window.open(host + oAuth2_segment_github, "_self")
}

export const loadUser = () => {
    axios.get("/api/login")
        .then(response => console.log(response.data));
}