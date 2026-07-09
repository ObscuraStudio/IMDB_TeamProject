import {useState} from "react";
import {useMovieFetch} from "../hooks/useMovieFetch.ts"
import {MIN_LENGTH_MOVIE_TITLE} from "../types/Movie.ts";
import NameIdQueryResult from "./NameIdQueryResult.tsx";

export default function MovieByName() {

    const placeholder: string = "Enter movie name";
    const [movieName, setMovieName] = useState<string>(placeholder);

    const {isSubmitButtonClicked, isMovieValid, movie, submit} = useMovieFetch();

    function submitMovieName(event: React.SubmitEvent) {
        event.preventDefault();
        const queryURI: string = "/title/";
        const pathSegment:string = movieName;
        submit(queryURI, pathSegment);
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
                       minLength={MIN_LENGTH_MOVIE_TITLE}
                       placeholder={placeholder}/>
                <br/>
                <button type={"submit"}> Submit</button>
            </form>
            {isSubmitButtonClicked &&
                <NameIdQueryResult isMovieValid={isMovieValid} movie={movie}></NameIdQueryResult>}
        </>
    )
}