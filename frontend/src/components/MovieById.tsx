import {useState} from "react";
import {baseURI, MIN_LENGTH_MOVIE_ID} from "../types/Movie.ts";
import {useMovieFetch} from "../hooks/useMovieFetch.ts";
import NameIdQueryResult from "./NameIdQueryResult.tsx";

export default function MovieById() {

    const placeholder: string = "Enter Imdb Id";
    const [movieId, setMovieId] = useState<string>(placeholder);

    const {isSubmitButtonClicked, isMovieValid, movie, submit} = useMovieFetch();

    function submitMovieId(event: React.SubmitEvent) {
        event.preventDefault();
        const queryURI: string = baseURI + "/" + movieId;
        submit(queryURI);
    }

    return (
        <>
            <form onSubmit={submitMovieId}>
                <label htmlFor="movie-id"> Movie name </label>
                <br/>
                <input onChange=
                           {(e) =>
                               setMovieId(e.target.value)
                           }
                       type="text"
                       id="movie-id"
                       name="Movie Id"
                       required
                       minLength={MIN_LENGTH_MOVIE_ID}
                       placeholder={placeholder}/>
                <br/>
                <button type={"submit"}> Submit</button>
            </form>
            {isSubmitButtonClicked &&
                <NameIdQueryResult isMovieValid={isMovieValid} movie={movie}></NameIdQueryResult>}
        </>
    )
}