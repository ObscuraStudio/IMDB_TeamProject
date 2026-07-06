import {useState} from "react";
import axios from "axios";
import type {OmdbMovieResponse} from "../types/OmdbMovie.ts";

export default function MovieByName() {
    const baseURI:string = "/api/movies";
    const [movieName, setMovieName] = useState<string>("movie123");
    const [movie, setMovie] = useState<OmdbMovieResponse>({Title: "", Year:""});
    const [isMovieValid, setIsMovieValid] = useState<boolean>(true);

    function submitMovieName(event:React.SubmitEvent){
        event.preventDefault();
        const queryURI:string = baseURI + "/title/" + movieName ;
        axios.get(queryURI)
            .then((response) =>
                (setMovie(response.data)))
            .catch(()=>setIsMovieValid(false));
    }

    return (
        <>
            <form onSubmit={submitMovieName}>
                <label>Movie name (minimum 5 characters) </label>
                <input onChange=
                           {(e) =>
                               setMovieName(e.target.value)
                           }
                    type="text"
                    id="Movie name"
                    name="Movie name"
                    required
                    minLength={4}
                    placeholder={"Enter movie name"}/>
                <button type={"submit"}> Submit </button>
            </form>
            <div>
                {isMovieValid ?
                    <p>Movie details: {movie.Title} Year of release: {movie.Year}</p> :
                    <p>Movie not found</p>
                }
            </div>
        </>
    )
}