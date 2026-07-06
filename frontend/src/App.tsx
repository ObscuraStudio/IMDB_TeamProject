import './App.css'
import {Route, Routes} from "react-router-dom";
import MovieByName from "./components/MovieByName.tsx";
import NavBar from "./components/NavBar.tsx";
import MovieById from "./components/MovieById.tsx";
import MoviePlotByName from "./components/MoviePlotByName.tsx";

function App() {


    return (
        <>
            <NavBar></NavBar>
            <Routes>
                <Route path={"/"} element={<MovieByName />}></Route>
                <Route path={"/findById"} element={<MovieById />}></Route>
                <Route path={"/findPlotByName"} element={<MoviePlotByName />}></Route>
            </Routes>
        </>
    )
}

export default App
