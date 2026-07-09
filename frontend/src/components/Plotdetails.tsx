import type {OmdbMovieResponse} from "../types/Movie.ts";
import "../css/MovieTable.css"

export default function Plotdetails(movie: Readonly<OmdbMovieResponse>) {
    return (
        <div>
            <article>
                <h1>
                    {movie.Title}
                </h1>
                <header>
                </header>
                <main>
                    <section id={"movie details"}>
                        <table className="interface-table">
                            <tbody>
                            <tr key="Plot" className="interface-row">
                                <th className="row-header">Plot</th>
                                <td className="interface-value">{movie.Plot}</td>
                            </tr>
                            </tbody>
                        </table>
                    </section>
                </main>
            </article>
        </div>
    )
}