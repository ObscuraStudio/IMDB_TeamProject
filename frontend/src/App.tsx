import './App.css'
import {Route, Routes} from "react-router-dom";
import MovieByName from "./components/MovieByName.tsx";
import NavBar from "./components/NavBar.tsx";
import MovieById from "./components/MovieById.tsx";
import MoviePlotByName from "./components/MoviePlotByName.tsx";
import Favourites from "./components/Favourites.tsx";
import LoginScreen from "./components/LoginScreen.tsx";
import {useEffect} from "react";
import {useAuth} from "./hooks/useAuth.ts";

function App() {

    const {login, logout, loadUser, user} = useAuth();

    useEffect(() => {
        loadUser();
    }, []);

    // Still checking the session — render nothing to avoid a login/app flash.
    if (user === undefined) {
        return <div className="loading-screen">Loading…</div>;
    }

    // Logged out: the login screen is the only thing the user can see.
    if (!user) {
        return <LoginScreen onLogin={login}/>;
    }

    // Logged in: full app with navigation, search and logout.
    return (
        <div className="app">
            <header className="top-bar">
                <span className="brand">🎬 Movie Database</span>
                <NavBar/>
                <div className="user-area">
                    <span className="welcome">Hi, {user}</span>
                    <button className="logout-btn" onClick={logout}>Logout</button>
                </div>
            </header>
            <main className="content">
                <Routes>
                    <Route path={"/"} element={<MovieByName/>}></Route>
                    <Route path={"/findById"} element={<MovieById/>}></Route>
                    <Route path={"/findPlotByName"} element={<MoviePlotByName/>}></Route>
                    <Route path={"/favourites"} element={<Favourites/>}></Route>
                </Routes>
            </main>
        </div>
    )
}

export default App
