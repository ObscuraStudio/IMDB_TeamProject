import axios from "axios";
import {useState} from "react";
import {oAuth2_segment_github, startURL_backend_local, startURL_frontend_local} from "../types/Authorization.ts";

export function useAuth() {
    // undefined = still checking, null = logged out, string = logged-in username
    const [user, setUser] = useState<string | undefined | null>(undefined);

    // In local dev the frontend (5173) must target the backend (8080) directly;
    // when deployed, frontend and backend share the same origin.
    function backendHost(): string {
        return window.location.host === startURL_frontend_local
            ? startURL_backend_local
            : window.location.origin;
    }

    function login() {
        window.open(backendHost() + oAuth2_segment_github, "_self");
    }

    function logout() {
        // Spring Security's default logout is a POST /logout. A top-level form
        // submit invalidates the session and follows the redirect back to the app
        // (no CSRF token needed since CSRF is disabled in SecurityConfig).
        const form = document.createElement("form");
        form.method = "POST";
        form.action = backendHost() + "/logout";
        document.body.appendChild(form);
        form.submit();
    }

    const loadUser = () => {
        axios.get("/api/auth/me")
            .then(response => setUser(response.data ? response.data : null))
            .catch(() => setUser(null));
    };

    return {login, logout, loadUser, user};
}
