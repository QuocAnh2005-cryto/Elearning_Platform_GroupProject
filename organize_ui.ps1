$src = "D:\SWP\Learner_UI\sceen_html"
$dst = "D:\SWP\Project\SE2034-SWP391-G4\src\main\resources"

# ── 1. Create all target directories ───────────────────────────────────────────
$dirs = @(
    "$dst\static\css",
    "$dst\static\js",
    "$dst\static\images",
    "$dst\templates\layout",
    "$dst\templates\auth",
    "$dst\templates\course",
    "$dst\templates\user",
    "$dst\templates\home",
    "$dst\templates\cart",
    "$dst\templates\learning",
    "$dst\templates\my_learning",
    "$dst\templates\purchase_history",
    "$dst\templates\recommendations",
    "$dst\templates\student_profile"
)
foreach ($d in $dirs) {
    New-Item -ItemType Directory -Force -Path $d | Out-Null
}
Write-Host "[OK] Directories created."

# ── 2. CSS files ───────────────────────────────────────────────────────────────
Copy-Item "$src\css\common\style.css"                       "$dst\static\css\style.css"              -Force
Copy-Item "$src\css\home\home.css"                          "$dst\static\css\home.css"               -Force
Copy-Item "$src\css\login\login.css"                        "$dst\static\css\auth.css"               -Force
Copy-Item "$src\css\courses\courses.css"                    "$dst\static\css\course.css"             -Force
Copy-Item "$src\css\profile\profile.css"                    "$dst\static\css\profile.css"            -Force
Copy-Item "$src\css\cart\cart.css"                          "$dst\static\css\cart.css"               -Force
Copy-Item "$src\css\learning\learning.css"                  "$dst\static\css\learning.css"           -Force
Copy-Item "$src\css\my_learning\my_learning.css"            "$dst\static\css\my_learning.css"        -Force
Copy-Item "$src\css\purchase_history\purchase_history.css"  "$dst\static\css\purchase_history.css"   -Force
Copy-Item "$src\css\recommendations\recommendations.css"    "$dst\static\css\recommendations.css"    -Force
Copy-Item "$src\css\student_profile\student_profile.css"    "$dst\static\css\student_profile.css"    -Force
Write-Host "[OK] CSS files copied (11 files)."

# ── 3. JS files ────────────────────────────────────────────────────────────────
Copy-Item "$src\javascript\common\script.js"                      "$dst\static\js\main.js"                 -Force
Copy-Item "$src\javascript\home\home.js"                          "$dst\static\js\home.js"                 -Force
Copy-Item "$src\javascript\home\home_logged_in.js"                "$dst\static\js\home_logged_in.js"       -Force
Copy-Item "$src\javascript\courses\courses.js"                    "$dst\static\js\course.js"               -Force
Copy-Item "$src\javascript\cart\cart.js"                          "$dst\static\js\cart.js"                 -Force
Copy-Item "$src\javascript\learning\learning.js"                  "$dst\static\js\learning.js"             -Force
Copy-Item "$src\javascript\my_learning\my_learning.js"            "$dst\static\js\my_learning.js"          -Force
Copy-Item "$src\javascript\profile\profile.js"                    "$dst\static\js\profile.js"              -Force
Copy-Item "$src\javascript\purchase_history\purchase_history.js"  "$dst\static\js\purchase_history.js"     -Force
Copy-Item "$src\javascript\recommendations\recommendations.js"    "$dst\static\js\recommendations.js"      -Force
Copy-Item "$src\javascript\student_profile\student_profile.js"    "$dst\static\js\student_profile.js"      -Force
Write-Host "[OK] JS files copied (11 files)."

# ── 4. Images ──────────────────────────────────────────────────────────────────
Copy-Item "$src\image\*" "$dst\static\images\" -Force
Write-Host "[OK] Images copied."

# ── 5. HTML Templates ──────────────────────────────────────────────────────────
Copy-Item "$src\index.html"                                        "$dst\templates\index.html"                              -Force
Copy-Item "$src\html\login\login.html"                             "$dst\templates\auth\login.html"                         -Force
Copy-Item "$src\html\courses\courses.html"                         "$dst\templates\course\list.html"                        -Force
Copy-Item "$src\html\course_detail\course_detail.html"             "$dst\templates\course\detail.html"                      -Force
Copy-Item "$src\html\profile\profile.html"                         "$dst\templates\user\profile.html"                       -Force
Copy-Item "$src\html\home\home.html"                               "$dst\templates\home\home.html"                          -Force
Copy-Item "$src\html\home\home_logged_in.html"                     "$dst\templates\home\home_logged_in.html"                -Force
Copy-Item "$src\html\cart\cart.html"                               "$dst\templates\cart\cart.html"                          -Force
Copy-Item "$src\html\learning\learning.html"                       "$dst\templates\learning\learning.html"                  -Force
Copy-Item "$src\html\my_learning\my_learning.html"                 "$dst\templates\my_learning\my_learning.html"            -Force
Copy-Item "$src\html\purchase_history\purchase_history.html"       "$dst\templates\purchase_history\purchase_history.html"  -Force
Copy-Item "$src\html\recommendations\recommendations.html"         "$dst\templates\recommendations\recommendations.html"    -Force
Copy-Item "$src\html\student_profile\student_profile.html"         "$dst\templates\student_profile\student_profile.html"    -Force
Write-Host "[OK] HTML templates copied (13 files)."

Write-Host ""
Write-Host "========================================="
Write-Host "   ALL FILES ORGANIZED SUCCESSFULLY!"
Write-Host "========================================="
