import './App.css'
import {Route, Routes} from "react-router-dom";
import MovieByName from "./components/MovieByName.tsx";
import NavBar from "./components/NavBar.tsx";
import MovieById from "./components/MovieById.tsx";
import MoviePlotByName from "./components/MoviePlotByName.tsx";
import {useEffect} from "react";
import login, {loadUser} from "./services/auth.ts";

function App() {

    useEffect(() => {
        loadUser();
    }, []);

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
