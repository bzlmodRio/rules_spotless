from get_spotless_group import get_spotless_group


def main():
    group = get_spotless_group()
    print(group.version)


if __name__ == "__main__":
    main()
