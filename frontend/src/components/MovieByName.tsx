import {useState} from "react";
import axios from "axios";
import {baseURI, MIN_LENGTH_MOVIE_TITLE, type OmdbMovieResponse} from "../types/Movie.ts";

export default function MovieByName() {
    const placeholder:string = "Enter movie name";
    const [movieName, setMovieName] = useState<string>(placeholder);
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
                <label> Movie name </label>
                <input onChange=
                           {(e) =>
                               setMovieName(e.target.value)
                           }
                       type="text"
                       id="Movie name"
                       name="Movie name"
                       required
                       minLength={MIN_LENGTH_MOVIE_TITLE}
                       placeholder={placeholder}/>
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