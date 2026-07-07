import {type JSX, useState} from "react";
import {baseURI, MIN_LENGTH_MOVIE_ID} from "../types/Movie.ts";
import MovieDetails from "./MovieDetails.tsx";
import {useMovieFetch} from "../hooks/useMovieFetch.ts";

export default function MovieById() {

    const placeholder: string = "Enter Imdb Id";
    const [movieId, setMovieId] = useState<string>(placeholder);

    const {isSubmitButtonClicked, isMovieValid, movie, submit} = useMovieFetch();

    function submitMovieId(event: React.SubmitEvent) {
        event.preventDefault();
        const queryURI: string = baseURI + "/" + movieId;
        submit(queryURI);
    }

    const renderResultContent = ():JSX.Element => {
        return (
            <div>
                {isMovieValid ?
                    <MovieDetails {...movie}/> :
                    <p>Movie with given Id not found</p>
                }
            </div>
        )
    }

    return (
        <>
            <form onSubmit={submitMovieId}>
                <label> Movie name </label>
                <br/>
                <input onChange=
                           {(e) =>
                               setMovieId(e.target.value)
                           }
                       type="text"
                       id="Movie Id"
                       name="Movie Id"
                       required
                       minLength={MIN_LENGTH_MOVIE_ID}
                       placeholder={placeholder}/>
                <br/>
                <button type={"submit"}> Submit</button>
            </form>
            {isSubmitButtonClicked && renderResultContent()}
        </>
    )
}