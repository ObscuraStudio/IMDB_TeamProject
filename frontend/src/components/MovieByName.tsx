import {useState} from "react";

export default function MovieByName() {

    const [movieName, setMovieName] = useState<string>("movie");

    return (
        <>
            <form>
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
                <button type={"submit"}></button>
            </form>
        </>
    )
}