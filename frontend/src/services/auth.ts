import {oAuth2_segment_github, startURL_backend_local, startURL_frontend_local} from "../types/Movie.ts";
import axios from "axios";

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