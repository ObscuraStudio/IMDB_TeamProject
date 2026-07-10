import type {OmdbMovieResponse} from "../types/Movie.ts";
import "../css/MovieTable.css"
import AddFavouriteButton from "./AddFavouriteButton.tsx";

export default function MovieDetails(movie: Readonly<OmdbMovieResponse>) {
    return (
        <div>
            <article>
                <header>
                    <h1>
                        {movie.Title}
                    </h1>
                </header>
                <main>
                    <section id={"movie details"}>
                        <table className="interface-table">
                            <tbody>
                            <tr key="Director" className="interface-row">
                                <th className="row-header">Director</th>
                                <td className="interface-value">{movie.Director}</td>
                            </tr>
                            <tr key="Year" className="interface-row">
                                <th className="row-header">Year</th>
                                <td className="interface-value">{movie.Year}</td>
                            </tr>
                            <tr key="Genre" className="interface-row">
                                <th className="row-header">Genre</th>
                                <td className="interface-value">{movie.Genre}</td>
                            </tr>
                            <tr key="Writer" className="interface-row">
                                <th className="row-header">Writer</th>
                                <td className="interface-value">{movie.Writer}</td>
                            </tr>
                            <tr key="Actors" className="interface-row">
                                <th className="row-header">Actors</th>
                                <td className="interface-value">{movie.Actors}</td>
                            </tr>
                            </tbody>
                        </table>
                    </section>
                </main>
                <AddFavouriteButton movie={movie}/>
            </article>
        </div>
    )
}