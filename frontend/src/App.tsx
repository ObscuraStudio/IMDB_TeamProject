import './App.css'
import {Route, Routes} from "react-router-dom";
import MovieByName from "./components/MovieByName.tsx";
import NavBar from "./components/NavBar.tsx";
import MovieById from "./components/MovieById.tsx";
import MoviePlotByName from "./components/MoviePlotByName.tsx";
import {oAuth2_segment_github, startURL_backend_local, startURL_frontend_local} from "./types/Movie.ts";

function App() {

    function login() {
        const host:string =
            window.location.host === startURL_frontend_local ?
                startURL_backend_local :
                window.location.origin;
        window.open(host + oAuth2_segment_github, "_self")
    }

    return (
        <>
            <NavBar></NavBar>
            <button onClick={login}>Login via GitHub</button>
            <Routes>
                <Route path={"/"} element={<MovieByName />}></Route>
                <Route path={"/findById"} element={<MovieById />}></Route>
                <Route path={"/findPlotByName"} element={<MoviePlotByName />}></Route>
            </Routes>
        </>
    )
}

export default App
