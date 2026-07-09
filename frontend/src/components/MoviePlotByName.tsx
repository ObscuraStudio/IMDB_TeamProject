import {type JSX, useState} from "react";
import {useMovieFetch} from "../hooks/useMovieFetch.ts";
import Plotdetails from "./Plotdetails.tsx";

export default function MoviePlotByName() {

    const placeholder: string = "Enter movie name";
    const [movieName, setMovieName] = useState<string>(placeholder);

    const {isSubmitButtonClicked, isMovieValid, movie, submit} = useMovieFetch();

    function submitMovieName(event: React.SubmitEvent) {
        event.preventDefault();
        const queryURI: string = "/title/";
        const pathSegment:string = movieName;
        submit(queryURI, pathSegment);
    }

    const returnPlotDetails = (): JSX.Element => {
        return (
            <>
                {isMovieValid ?
                    <Plotdetails {...movie}></Plotdetails> :
                    <p>Movie with given name not found</p>
                }
            </>
        )
    }

    return (
        <>
            <form onSubmit={submitMovieName}>
                <label htmlFor={"movie-name"}> Movie name </label>
                <br/>
                <input onChange=
                           {(e) =>
                               setMovieName(e.target.value)
                           }
                       type="text"
                       id="movie-name"
                       name="Movie name"
                       required
                       placeholder={placeholder}/>
                <br/>
                <button type={"submit"}> Submit</button>
            </form>
            {isSubmitButtonClicked && returnPlotDetails()}
        </>
    )
}