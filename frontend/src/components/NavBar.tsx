import {NavLink} from "react-router-dom";

export default function NavBar() {
    const linkClass = ({isActive}: {isActive: boolean}) =>
        isActive ? "nav-link active" : "nav-link";

    return (
        <nav className="nav">
            <NavLink to={"/"} className={linkClass} end>Find by Name</NavLink>
            <NavLink to={"/findById"} className={linkClass}>Find by Id</NavLink>
            <NavLink to={"/findPlotByName"} className={linkClass}>Find Plot by Name</NavLink>
            <NavLink to={"/favourites"} className={linkClass}>My Favourites</NavLink>
        </nav>
    )
}
