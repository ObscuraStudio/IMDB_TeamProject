import axios from "axios";
import {useState} from "react";
import {oAuth2_segment_github, startURL_backend_local, startURL_frontend_local} from "../types/Authorization.ts";


export function useAuth() {
    const [user, setUser] = useState<string | undefined | null>(undefined);

    function login() {
        const host: string =
            window.location.host === startURL_frontend_local ?
                startURL_backend_local :
                window.location.origin;
        window.open(host + oAuth2_segment_github, "_self")
    }

    const loadUser = () => {
        axios.get("/api/login")
            .then(response => setUser(response.data))
            .catch(() => setUser(null));
    }

    return {login, loadUser, user};
}