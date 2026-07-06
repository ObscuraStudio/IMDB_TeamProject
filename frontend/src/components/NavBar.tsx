import {Link} from "react-router-dom";

export default function NavBar() {
    return (
        <>
            <Link to={"/"}>Find Movie By Name</Link>
            <Link to={"/findById"}>Find Movie By Id</Link>
            <Link to={"/findPlotByName"}>Find Movie Plot By Name</Link>
        </>
    )
}