import './App.css'
import {Route, Routes} from "react-router-dom";
import Homepage from "./components/Homepage.tsx";
import NavBar from "./components/NavBar.tsx";

function App() {
    

    return (
        <>
            <NavBar></NavBar>
            <Routes>
                <Route path={"/"} element={<Homepage />}></Route>
            </Routes>
        </>
    )
}

export default App
