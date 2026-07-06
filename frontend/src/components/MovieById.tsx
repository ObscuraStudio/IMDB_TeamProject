import {useState} from "react";
import {baseURI, MIN_LENGTH_MOVIE_ID, type OmdbMovieResponse} from "../types/Movie.ts";
import axios from "axios";
import MovieDetails from "./MovieDetails.tsx";

export default function MovieById() {
    const placeholder: string = "Enter Imdb Id";
    const [movieId, setMovieId] = useState<string>(placeholder);
    const [movie, setMovie] = useState<OmdbMovieResponse>({Title: "", Year: ""});
    const [isMovieValid, setIsMovieValid] = useState<boolean>(true);

    function submitMovieId(event: React.SubmitEvent) {
        event.preventDefault();
        const queryURI: string = baseURI + "/" + movieId;
        axios.get(queryURI)
            .then((response) => {
                setMovie(response.data);
                setIsMovieValid(true);
            })
            .catch(() => setIsMovieValid(false));
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
            <div>
                {isMovieValid ?
                    <MovieDetails {...movie}/> :
                    <p>Movie not found</p>
                }
            </div>
        </>
    )
}