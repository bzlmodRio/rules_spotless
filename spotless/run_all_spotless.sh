
cd $BUILD_WORKSPACE_DIRECTORY

bazel query "attr(tags, '_spotless_apply', //...)" --ui_event_filters=stdout --noshow_progress > temp_spotless.txt

cat temp_spotless.txt

while read p; do
  bazel run $p
done <temp_spotless.txt

rm temp_spotless.txt

echo "Done"