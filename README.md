# zucchetti-utils

Calisthenics to solve a a real life problem.

From a string containing a sequence of times in the format of
Zucchetti's digital signing in software, print a line with the time
worked, and how much overtime has been accrued, based on a 8h work
day, with 15' of rounding.

I'm following the functional calisthenics rules explained here:

https://codurance.com/2017/10/12/functional-calisthenics/

which sums up to:

. Name everything
. No mutable state
. Exhaustive conditionals
. Do not use intermediate variables
. Expressions not statements
. No Explicit recursion
. Generic building blocks
. Side effects at the boundaries
. Infinite Sequences
. One argument functions

I'm making an exception for tests, though, which will be exempt. Also
the `partial` function ought to be excluded from the "single argument
functions" (I admit I didn't got that rule that well anyway).

## Installation

Either run with lein run, or create a jar/überjar and use that.

## Usage

    echo "12.30 13.35" | lein run

should print something like:

    Overtime: -7.00 Total: 1.05

(where 5 minutes have been correctly ignored).

### Bugs

Errors are very generic, so it's unclear why some inputs are rejected
(there are some rules, like even number of input hours, values must be
increasing, ecc…).

## License

Copyright © 2017 Riccardo Di Meo

This software can be freely used and modified without further
permission from the author. Attribution is appreciated but not required.

Although crafted with the best intentions in mind, this software is
unfit for any use or purpose (including a possible employment as
learning prop).

NEITHER RECIPIENT NOR ANY CONTRIBUTORS SHALL HAVE ANY LIABILITY FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING WITHOUT LIMITATION LOST PROFITS), HOWEVER CAUSED
AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
THE USE OR DISTRIBUTION OF THE PROGRAM OR THE EXERCISE OF ANY RIGHTS
GRANTED HEREUNDER, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.





